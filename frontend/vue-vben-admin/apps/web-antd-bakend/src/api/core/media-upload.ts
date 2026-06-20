import { requestClient } from '#/api/request';

/** 媒体服务网关前缀 */
const BASE = '/cloud-media-asset-processing/api/media';

/** 文件类型枚举 */
export type FileType = 'IMAGE' | 'VIDEO' | 'AUDIO' | 'DOCUMENT' | 'OTHER';

/** 媒体状态枚举 */
export type MediaStatus = 'PENDING' | 'UPLOADING' | 'UPLOADED' | 'FAILED';

/** 预签名上传响应 */
export interface PresignedUrlResponse {
  objectName: string;
  presignedUrl: string;
  expires: number;
}

/** 分片上传初始化响应 */
export interface InitiateMultipartUploadResponse {
  uploadId: string;
  chunkUrls: string[];
  chunkSize: number;
  totalChunks: number;
}

/** 上传进度回调 */
export type OnProgress = (progress: number, uploadedSize: number, totalSize: number) => void;

/** 上传配置选项 */
export interface UploadOptions {
  /** 文件 */
  file: File;
  /** 文件类型 */
  fileType: FileType;
  /** 文件md5（可选） */
  fileMd5?: string;
  /** 分片大小（字节），默认5MB，用于分片上传 */
  chunkSize?: number;
  /** 并发上传数（分片上传时），默认3 */
  concurrentUploads?: number;
  /** 进度回调 */
  onProgress?: OnProgress;
  /** 错误回调 */
  onError?: (error: Error) => void;
  /** 成功回调 */
  onSuccess?: (assetId: number) => void;
}

/** 上传结果 */
export interface UploadResult {
  assetId: number;
  objectName: string;
  fileName: string;
}

/**
 * 计算文件的MD5（分片计算，避免大文件卡顿）
 */
async function calculateFileMd5(file: File, onProgress?: (progress: number) => void): Promise<string> {
  return new Promise((resolve, reject) => {
    const spark = new (window as any).SparkMD5.ArrayBuffer();
    const reader = new FileReader();
    const chunkSize = 2 * 1024 * 1024; // 2MB per chunk
    let offset = 0;
    const chunks = Math.ceil(file.size / chunkSize);

    reader.onload = (e) => {
      spark.append(e.target?.result as ArrayBuffer);
      offset += chunkSize;
      if (onProgress) {
        onProgress(Math.min(Math.round((offset / file.size) * 100), 100));
      }
      if (offset < file.size) {
        readNextChunk();
      } else {
        resolve(spark.end());
      }
    };

    reader.onerror = () => reject(new Error('Failed to read file'));

    function readNextChunk() {
      const slice = file.slice(offset, offset + chunkSize);
      reader.readAsArrayBuffer(slice);
    }

    readNextChunk();
  });
}

/**
 * 普通上传（适用于小文件，直接获取预签名URL上传）
 * @param file 文件
 * @param fileType 文件类型
 * @param fileMd5 文件md5（可选）
 * @param onProgress 进度回调
 */
async function getPresignedUploadUrl(
  file: File,
  fileType: FileType,
  fileMd5?: string,
  onProgress?: OnProgress
): Promise<{ objectName: string; presignedUrl: string }> {
  const response = await requestClient.post<PresignedUrlResponse>(
    `${BASE}/presign`,
    {
      fileName: file.name,
      fileSize: file.size,
      contentType: file.type,
      fileMd5,
      fileType,
    }
  );
  return { objectName: response.objectName, presignedUrl: response.presignedUrl };
}

/**
 * 完成普通上传
 * @param objectName 文件对象名
 */
async function completePresignedUpload(objectName: string): Promise<number> {
  return requestClient.post<number>(`${BASE}/presign/complete`, null, {
    params: { objectName },
  });
}

/**
 * 初始化分片上传
 * @param file 文件
 * @param fileType 文件类型
 * @param fileMd5 文件md5（可选）
 */
async function initiateMultipartUpload(
  file: File,
  fileType: FileType,
  fileMd5?: string
): Promise<InitiateMultipartUploadResponse> {
  return requestClient.post<InitiateMultipartUploadResponse>(`${BASE}/initiate`, {
    fileName: file.name,
    fileSize: file.size,
    contentType: file.type,
    fileMd5,
    fileType,
  });
}

/**
 * 完成分片上传
 * @param uploadId 分片上传ID
 */
async function completeMultipartUpload(uploadId: string): Promise<number> {
  return requestClient.post<number>(`${BASE}/complete`, null, {
    params: { uploadId },
  });
}

/**
 * 上传文件到预签名URL
 * @param presignedUrl 预签名URL
 * @param file 文件
 * @param onProgress 进度回调
 */
async function uploadToPresignedUrl(
  presignedUrl: string,
  file: File,
  onProgress?: OnProgress
): Promise<void> {
  await new Promise<void>((resolve, reject) => {
    const xhr = new XMLHttpRequest();

    xhr.upload.onprogress = (e) => {
      if (e.lengthComputable && onProgress) {
        onProgress(Math.round((e.loaded / e.total) * 100), e.loaded, e.total);
      }
    };

    xhr.onload = () => {
      if (xhr.status >= 200 && xhr.status < 300) {
        resolve();
      } else {
        reject(new Error(`Upload failed with status ${xhr.status}`));
      }
    };

    xhr.onerror = () => reject(new Error('Network error'));
    xhr.open('PUT', presignedUrl);
    xhr.setRequestHeader('Content-Type', file.type);
    xhr.send(file);
  });
}

/**
 * 上传分片到预签名URL
 * @param chunkUrl 分片预签名URL
 * @param chunk 分片数据
 * @param onProgress 进度回调
 */
async function uploadChunkToPresignedUrl(
  chunkUrl: string,
  chunk: Blob,
  onProgress?: (progress: number) => void
): Promise<void> {
  await new Promise<void>((resolve, reject) => {
    const xhr = new XMLHttpRequest();

    xhr.upload.onprogress = (e) => {
      if (e.lengthComputable && onProgress) {
        onProgress(Math.round((e.loaded / e.total) * 100));
      }
    };

    xhr.onload = () => {
      if (xhr.status >= 200 && xhr.status < 300) {
        resolve();
      } else {
        reject(new Error(`Chunk upload failed with status ${xhr.status}`));
      }
    };

    xhr.onerror = () => reject(new Error('Network error'));
    xhr.open('PUT', chunkUrl);
    xhr.send(chunk);
  });
}

/**
 * 分片上传文件
 * @param file 文件
 * @param chunkSize 分片大小（字节）
 * @param concurrentUploads 并发上传数
 * @param uploadId 分片上传ID
 * @param chunkUrls 分片预签名URL数组
 * @param onProgress 进度回调
 */
async function uploadInChunks(
  file: File,
  chunkSize: number,
  concurrentUploads: number,
  uploadId: string,
  chunkUrls: string[],
  onProgress?: OnProgress
): Promise<void> {
  const chunks: { index: number; start: number; end: number }[] = [];
  let offset = 0;
  let chunkIndex = 0;

  while (offset < file.size) {
    const start = offset;
    const end = Math.min(offset + chunkSize, file.size);
    chunks.push({ index: chunkIndex++, start, end });
    offset = end;
  }

  let completedChunks = 0;
  const totalChunks = chunks.length;

  async function uploadChunkWithRetry(
    chunk: { index: number; start: number; end: number },
    maxRetries = 3
  ): Promise<void> {
    for (let i = 0; i < maxRetries; i++) {
      try {
        const blob = file.slice(chunk.start, chunk.end);
        const chunkUrl = chunkUrls[chunk.index];
        if (!chunkUrl) {
          throw new Error(`Missing presigned URL for chunk ${chunk.index}`);
        }
        await uploadChunkToPresignedUrl(chunkUrl, blob);
        completedChunks++;
        if (onProgress) {
          const totalProgress = Math.round((completedChunks / totalChunks) * 100);
          onProgress(totalProgress, completedChunks * chunkSize, file.size);
        }
        return;
      } catch (error) {
        if (i === maxRetries - 1) throw error;
        await new Promise((r) => setTimeout(r, 1000 * (i + 1))); // 指数退避
      }
    }
  }

  // 使用并发控制上传分片
  const queue = [...chunks];
  const uploading: Promise<void>[] = [];
  const completedSet = new Set<number>();

  while (queue.length > 0 || uploading.length > 0) {
    while (uploading.length < concurrentUploads && queue.length > 0) {
      const chunk = queue.shift()!;
      const chunkIndex = chunk.index;
      uploading.push(
        uploadChunkWithRetry(chunk)
          .then(() => { completedSet.add(chunkIndex); })
          .catch(() => {})
      );
    }
    if (uploading.length > 0) {
      await Promise.race(uploading);
      const prevSize = completedSet.size;
      uploading.splice(
        uploading.findIndex(() => completedSet.size > prevSize),
        1
      );
    }
  }

  // 确保所有分片都上传成功
  await Promise.all(uploading);
}

/**
 * 判断是否应该使用分片上传
 * @param fileSize 文件大小（字节）
 * @param threshold 阈值（字节），默认5MB
 */
function shouldUseMultipartUpload(fileSize: number, threshold = 5 * 1024 * 1024): boolean {
  return fileSize > threshold;
}

/**
 * 格式化文件大小
 * @param bytes 字节数
 */
export function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return `${parseFloat((bytes / Math.pow(k, i)).toFixed(2))} ${sizes[i]}`;
}

/**
 * 获取文件类型
 * @param mimeType 文件MIME类型
 */
export function getFileType(mimeType: string): FileType {
  if (mimeType.startsWith('image/')) return 'IMAGE';
  if (mimeType.startsWith('video/')) return 'VIDEO';
  if (mimeType.startsWith('audio/')) return 'AUDIO';
  if (
    mimeType.includes('pdf') ||
    mimeType.includes('document') ||
    mimeType.includes('msword') ||
    mimeType.includes('openxmlformats')
  )
    return 'DOCUMENT';
  return 'OTHER';
}

/**
 * 媒体文件上传客户端
 *
 * 支持两种上传方式：
 * 1. 普通上传（适用于小文件，获取预签名URL直接上传到MinIO）
 * 2. 分片上传（适用于大文件，先初始化分片上传，再逐个上传分片）
 *
 * @example
 * ```ts
 * const client = new MediaUploadClient();
 *
 * // 上传文件（自动选择上传方式）
 * const result = await client.upload({
 *   file: file,
 *   fileType: 'IMAGE',
 *   onProgress: (progress) => console.log(`上传进度: ${progress}%`)
 * });
 *
 * console.log(`上传成功，资产ID: ${result.assetId}`);
 * ```
 */
export class MediaUploadClient {
  private defaultChunkSize = 5 * 1024 * 1024; // 5MB
  private defaultConcurrentUploads = 3;
  private multipartThreshold = 5 * 1024 * 1024; // 5MB

  constructor(options?: {
    chunkSize?: number;
    concurrentUploads?: number;
    multipartThreshold?: number;
  }) {
    if (options?.chunkSize) this.defaultChunkSize = options.chunkSize;
    if (options?.concurrentUploads) this.defaultConcurrentUploads = options.concurrentUploads;
    if (options?.multipartThreshold) this.multipartThreshold = options.multipartThreshold;
  }

  /**
   * 上传文件（自动选择普通上传或分片上传）
   */
  async upload(options: UploadOptions): Promise<UploadResult> {
    const {
      file,
      fileType,
      fileMd5,
      chunkSize = this.defaultChunkSize,
      concurrentUploads = this.defaultConcurrentUploads,
      onProgress,
      onError,
      onSuccess,
    } = options;

    try {
      let assetId: number;

      if (shouldUseMultipartUpload(file.size, this.multipartThreshold)) {
        // 分片上传
        assetId = await this.multipartUpload(
          file,
          fileType,
          fileMd5,
          chunkSize,
          concurrentUploads,
          onProgress
        );
      } else {
        // 普通上传
        assetId = await this.presignedUpload(file, fileType, fileMd5, onProgress);
      }

      onSuccess?.(assetId);
      return {
        assetId,
        objectName: file.name,
        fileName: file.name,
      };
    } catch (error) {
      const err = error instanceof Error ? error : new Error(String(error));
      onError?.(err);
      throw err;
    }
  }

  /**
   * 普通上传（预签名URL方式）
   */
  async presignedUpload(
    file: File,
    fileType: FileType,
    fileMd5?: string,
    onProgress?: OnProgress
  ): Promise<number> {
    // 1. 获取预签名URL
    const { objectName, presignedUrl } = await getPresignedUploadUrl(
      file,
      fileType,
      fileMd5,
      onProgress
    );

    // 2. 上传文件到预签名URL
    await uploadToPresignedUrl(presignedUrl, file, onProgress);

    // 3. 完成上传
    const assetId = await completePresignedUpload(objectName);

    return assetId;
  }

  /**
   * 分片上传
   */
  async multipartUpload(
    file: File,
    fileType: FileType,
    fileMd5?: string,
    chunkSize?: number,
    concurrentUploads?: number,
    onProgress?: OnProgress
  ): Promise<number> {
    // 1. 初始化分片上传
    const { uploadId, chunkUrls } = await initiateMultipartUpload(file, fileType, fileMd5);

    // 2. 分片上传
    await uploadInChunks(
      file,
      chunkSize || this.defaultChunkSize,
      concurrentUploads || this.defaultConcurrentUploads,
      uploadId,
      chunkUrls,
      onProgress
    );

    // 3. 完成分片上传并获取assetId
    const assetId = await completeMultipartUpload(uploadId);

    return assetId;
  }
}

/**
 * 默认的上传客户端实例
 */
export const defaultMediaUploadClient = new MediaUploadClient();

/**
 * 快速上传文件（使用默认客户端）
 */
export async function uploadMediaFile(options: UploadOptions): Promise<UploadResult> {
  return defaultMediaUploadClient.upload(options);
}
