import type { VxeTableGridColumns } from '#/adapter/vxe-table';
import type { AssetVO } from '#/api/core/media-asset';

/** 状态选项 */
const statusOptions = [
  { label: '上传中', value: 'UPLOADING' },
  { label: '已上传', value: 'UPLOADED' },
  { label: '上传失败', value: 'FAILED' },
  { label: '处理中', value: 'PROCESSING' },
  { label: '已处理', value: 'PROCESSED' },
];

/**
 * 表格列定义
 */
export function useColumns(): VxeTableGridColumns<AssetVO> {
  return [
    { type: 'checkbox', width: 50 },
    { field: 'id', title: 'ID', width: 120 },
    { field: 'fileName', title: '文件名', minWidth: 180 },
    { field: 'fileType', title: '文件类型', width: 120, align: 'center' },
    { field: 'fileSizeFormatted', title: '文件大小', width: 100, align: 'center' },
    { field: 'bucketName', title: '存储桶', width: 100, align: 'center' },
    {
      align: 'center',
      field: 'status',
      slots: { default: 'status' },
      title: '状态',
      width: 100,
    },
    { field: 'uploaderId', title: '上传者', width: 100, align: 'center' },
    {
      field: 'createTime',
      title: '创建时间',
      width: 170,
    },
    {
      field: 'action',
      fixed: 'right',
      headerAlign: 'center',
      title: '操作',
      width: 180,
      slots: { default: 'action' },
    },
  ];
}

/**
 * 搜索表单 schema
 */
export function useGridFormSchema() {
  return [
    {
      component: 'Input',
      fieldName: 'fileName',
      formFieldProps: { label: '文件名' },
      componentProps: { placeholder: '请输入文件名' },
    },
    {
      component: 'Select',
      fieldName: 'status',
      formFieldProps: { label: '状态' },
      componentProps: {
        options: [
          { label: '全部', value: undefined },
          ...statusOptions,
        ],
        placeholder: '选择状态',
      },
    },
    {
      component: 'Input',
      fieldName: 'fileType',
      formFieldProps: { label: '文件类型' },
      componentProps: { placeholder: '如 video/mp4' },
    },
  ];
}

/**
 * 新增/编辑表单 schema
 */
export function useFormSchema() {
  return [
    {
      component: 'Input',
      fieldName: 'fileName',
      label: '文件名',
      rules: 'required',
      componentProps: { placeholder: '请输入原始文件名' },
    },
    {
      component: 'Input',
      fieldName: 'objectName',
      label: '对象名',
      rules: 'required',
      componentProps: { placeholder: 'MinIO 存储对象名' },
    },
    {
      component: 'Input',
      fieldName: 'bucketName',
      label: '存储桶',
      defaultValue: 'cloud',
      rules: 'required',
      componentProps: { placeholder: '如 cloud' },
    },
    {
      component: 'Input',
      fieldName: 'fileType',
      label: 'MIME类型',
      componentProps: { placeholder: '如 video/mp4' },
    },
    {
      component: 'InputNumber',
      fieldName: 'fileSize',
      label: '文件大小(字节)',
      componentProps: { placeholder: '文件字节数', min: 0, style: { width: '100%' } },
    },
    {
      component: 'Input',
      fieldName: 'fileMd5',
      label: 'MD5',
      componentProps: { placeholder: '文件MD5哈希值' },
    },
    {
      component: 'Input',
      fieldName: 'uploaderId',
      label: '上传者ID',
      componentProps: { placeholder: '用户ID' },
    },
    {
      component: 'Select',
      fieldName: 'status',
      label: '状态',
      defaultValue: 'UPLOADED',
      rules: 'required',
      componentProps: {
        options: statusOptions,
        placeholder: '选择状态',
      },
    },
    {
      component: 'Input',
      fieldName: 'thumbnailName',
      label: '缩略图对象名',
      componentProps: { placeholder: '缩略图 MinIO 对象名' },
    },
    {
      component: 'InputNumber',
      fieldName: 'width',
      label: '宽度(像素)',
      componentProps: { placeholder: '像素', min: 0, style: { width: '100%' } },
    },
    {
      component: 'InputNumber',
      fieldName: 'height',
      label: '高度(像素)',
      componentProps: { placeholder: '像素', min: 0, style: { width: '100%' } },
    },
    {
      component: 'InputNumber',
      fieldName: 'duration',
      label: '时长(秒)',
      componentProps: { placeholder: '视频时长秒数', min: 0, style: { width: '100%' } },
    },
  ];
}
