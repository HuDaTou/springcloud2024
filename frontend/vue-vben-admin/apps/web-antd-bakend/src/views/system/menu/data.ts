import type { OnActionClickFn, VxeTableGridColumns } from '#/adapter/vxe-table';
import type { MenuApi } from '#/api/core/menu';

/**
 * 表格列定义
 */
export function useColumns(
  onActionClick: OnActionClickFn<MenuApi.MenuListVO>,
): VxeTableGridColumns<MenuApi.MenuListVO> {
  return [
    { type: 'checkbox', width: 50 },
    { field: 'id', title: 'ID', width: 70, sortable: true },
    {
      align: 'left',
      field: 'title',
      fixed: 'left',
      minWidth: 180,
      title: '菜单名称',
      treeNode: true,
    },
    { field: 'name', title: '路由名称', minWidth: 140 },
    { field: 'path', title: '路由路径', minWidth: 160 },
    {
      align: 'center',
      field: 'icon',
      title: '图标',
      width: 80,
    },
    {
      field: 'component',
      minWidth: 180,
      title: '组件路径',
    },
    {
      align: 'center',
      field: 'orderNum',
      sortable: true,
      title: '排序',
      width: 80,
    },
    {
      align: 'center',
      field: 'isDisable',
      slots: { default: 'isDisable' },
      title: '状态',
      width: 80,
    },
    {
      field: 'createTime',
      title: '创建时间',
      width: 170,
    },
    {
      align: 'center',
      cellRender: {
        attrs: {
          nameField: 'title',
          onClick: onActionClick,
        },
        name: 'CellOperation',
        options: [
          { code: 'append', text: '添加子菜单' },
          'edit',
          'delete',
        ],
      },
      field: 'operation',
      fixed: 'right',
      headerAlign: 'center',
      showOverflow: false,
      title: '操作',
      width: 240,
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
      fieldName: 'title',
      formFieldProps: { label: '菜单名称' },
      componentProps: { placeholder: '请输入菜单名称' },
    },
    {
      component: 'Select',
      fieldName: 'isDisable',
      formFieldProps: { label: '状态' },
      componentProps: {
        options: [
          { label: '全部', value: undefined },
          { label: '正常', value: 0 },
          { label: '禁用', value: 1 },
        ],
        placeholder: '选择状态',
      },
    },
  ];
}

/**
 * 新增/编辑表单 schema
 */
export function useFormSchema(parentMenuTreeData: MenuApi.MenuListVO[]) {
  return [
    {
      component: 'TreeSelect',
      fieldName: 'parentId',
      label: '父级菜单',
      componentProps: {
        placeholder: '请选择父级菜单（留空为顶级菜单）',
        treeDefaultExpandAll: true,
        allowClear: true,
        treeData: parentMenuTreeData,
        fieldNames: {
          label: 'title',
          value: 'id',
          children: 'children',
        },
      },
    },
    {
      component: 'Input',
      fieldName: 'title',
      label: '菜单名称',
      rules: 'required',
      componentProps: { placeholder: '请输入菜单名称' },
    },
    {
      component: 'Input',
      fieldName: 'name',
      label: '路由名称',
      componentProps: { placeholder: '请输入路由名称' },
    },
    {
      component: 'Input',
      fieldName: 'path',
      label: '路由路径',
      componentProps: { placeholder: '请输入路由路径，如 /system' },
    },
    {
      component: 'Input',
      fieldName: 'component',
      label: '组件路径',
      componentProps: { placeholder: '请输入组件路径，如 system/menu/list' },
    },
    {
      component: 'Input',
      fieldName: 'icon',
      label: '图标',
      componentProps: { placeholder: '请输入图标名称' },
    },
    {
      component: 'InputNumber',
      fieldName: 'orderNum',
      label: '排序号',
      defaultValue: 0,
      componentProps: { placeholder: '数字越小越靠前', min: 0, style: { width: '100%' } },
    },
    {
      component: 'Input',
      fieldName: 'redirect',
      label: '重定向',
      componentProps: { placeholder: '请输入重定向路径' },
    },
    {
      component: 'Input',
      fieldName: 'url',
      label: '外部链接',
      componentProps: { placeholder: '请输入外部链接地址' },
    },
    {
      component: 'Select',
      fieldName: 'target',
      label: '打开方式',
      componentProps: {
        options: [
          { label: '当前窗口', value: '_self' },
          { label: '新窗口', value: '_blank' },
        ],
        placeholder: '请选择打开方式',
        allowClear: true,
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'hideInMenu',
      label: '隐藏菜单',
      defaultValue: 0,
      componentProps: {
        options: [
          { label: '显示', value: 0 },
          { label: '隐藏', value: 1 },
        ],
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'hideInBreadcrumb',
      label: '隐藏面包屑',
      defaultValue: 0,
      componentProps: {
        options: [
          { label: '显示', value: 0 },
          { label: '隐藏', value: 1 },
        ],
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'hideChildrenInMenu',
      label: '隐藏子菜单',
      defaultValue: 0,
      componentProps: {
        options: [
          { label: '显示', value: 0 },
          { label: '隐藏', value: 1 },
        ],
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'keepAlive',
      label: '路由缓存',
      defaultValue: 0,
      componentProps: {
        options: [
          { label: '关闭', value: 0 },
          { label: '开启', value: 1 },
        ],
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'affix',
      label: '固定标签',
      defaultValue: 0,
      componentProps: {
        options: [
          { label: '不固定', value: 0 },
          { label: '固定', value: 1 },
        ],
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'isDisable',
      label: '状态',
      defaultValue: false,
      componentProps: {
        options: [
          { label: '正常', value: false },
          { label: '禁用', value: true },
        ],
      },
    },
  ];
}
