import type { VxeTableGridColumns } from '#/adapter/vxe-table';
import type { RulesVO } from '#/api/core/file-upload-rules';

/**
 * 表格列定义
 */
export function useColumns(): VxeTableGridColumns<RulesVO> {
  return [
    { type: 'checkbox', width: 50 },
    { field: 'id', title: 'ID', width: 120 },
    { field: 'ruleName', title: '规则名称', minWidth: 140 },
    { field: 'description', title: '描述', minWidth: 180 },
    { field: 'maxSizeKb', title: '最大大小(KB)', width: 120, align: 'center' },
    { field: 'storagePath', title: '存储路径', minWidth: 160 },
    {
      align: 'center',
      field: 'isActive',
      slots: { default: 'isActive' },
      title: '状态',
      width: 80,
    },
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
      width: 160,
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
      fieldName: 'ruleName',
      formFieldProps: { label: '规则名称' },
      componentProps: { placeholder: '请输入规则名称' },
    },
    {
      component: 'Select',
      fieldName: 'isActive',
      formFieldProps: { label: '状态' },
      componentProps: {
        options: [
          { label: '全部', value: undefined },
          { label: '启用', value: true },
          { label: '禁用', value: false },
        ],
        placeholder: '选择状态',
      },
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
      fieldName: 'ruleName',
      label: '规则名称',
      rules: 'required',
      componentProps: { placeholder: '请输入规则名称，如 AVATAR_UPLOAD' },
    },
    {
      component: 'Textarea',
      fieldName: 'description',
      label: '描述信息',
      componentProps: { placeholder: '请输入规则描述信息', rows: 3 },
    },
    {
      component: 'InputNumber',
      fieldName: 'maxSizeKb',
      label: '最大文件大小(KB)',
      rules: 'required',
      defaultValue: 5120,
      componentProps: { placeholder: '单位KB', min: 1, style: { width: '100%' } },
    },
    {
      component: 'Select',
      fieldName: 'fileCategory',
      label: '文件分类',
      componentProps: {
        options: [
          { label: 'IMAGE', value: 'IMAGE' },
          { label: 'DOCUMENT', value: 'DOCUMENT' },
          { label: 'VIDEO', value: 'VIDEO' },
          { label: 'AUDIO', value: 'AUDIO' },
          { label: 'AVATAR', value: 'AVATAR' },
        ],
        placeholder: '请选择文件分类',
        mode: 'multiple',
      },
    },
    {
      component: 'Select',
      fieldName: 'allowedExtensions',
      label: '允许的MIME类型',
      componentProps: {
        options: [
          { label: 'image/jpeg', value: 'image/jpeg' },
          { label: 'image/png', value: 'image/png' },
          { label: 'image/gif', value: 'image/gif' },
          { label: 'image/webp', value: 'image/webp' },
          { label: 'application/pdf', value: 'application/pdf' },
          { label: 'application/msword', value: 'application/msword' },
          { label: 'video/mp4', value: 'video/mp4' },
          { label: 'audio/mpeg', value: 'audio/mpeg' },
          { label: 'text/plain', value: 'text/plain' },
        ],
        placeholder: '请选择允许的MIME类型',
        mode: 'multiple',
      },
    },
    {
      component: 'Input',
      fieldName: 'storagePath',
      label: '存储路径',
      componentProps: { placeholder: '如 uploads/images/' },
    },
    {
      component: 'Input',
      fieldName: 'storagePrefix',
      label: '存储前缀',
      componentProps: { placeholder: '如 avatars/' },
    },
    {
      component: 'RadioGroup',
      fieldName: 'isActive',
      label: '状态',
      defaultValue: true,
      componentProps: {
        options: [
          { label: '启用', value: true },
          { label: '禁用', value: false },
        ],
      },
    },
  ];
}
