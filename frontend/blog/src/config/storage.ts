export const storageConfig = {
  minio: {
    endpoint: import.meta.env.VITE_MINIO_ENDPOINT || 'http://localhost:9000',
    bucket: import.meta.env.VITE_MINIO_BUCKET || 'cloud'
  }
}

export function getStorageUrl(path: string): string {
  return `${storageConfig.minio.endpoint}/${storageConfig.minio.bucket}/${path}`
}