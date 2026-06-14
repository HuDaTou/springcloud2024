import type { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    meta: {
      icon: 'lucide:settings',
      order: 100,
      title: '系统管理',
    },
    name: 'System',
    path: '/system',
    children: [
      {
        name: 'SystemUser',
        path: '/user',
        component: () => import('#/views/system/user/list.vue'),
        meta: {
          icon: 'lucide:users',
          title: '用户管理',
        },
      },
      {
        name: 'SystemMenu',
        path: '/menu',
        component: () => import('#/views/system/menu/list.vue'),
        meta: {
          icon: 'lucide:menu',
          title: '菜单管理',
        },
      },
      {
        name: 'SystemPermission',
        path: '/permission',
        component: () => import('#/views/system/permission/list.vue'),
        meta: {
          icon: 'lucide:shield-check',
          title: '权限管理',
        },
      },
    ],
  },
];

export default routes;
