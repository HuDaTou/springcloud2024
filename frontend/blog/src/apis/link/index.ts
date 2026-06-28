import http from "@/utils/http.ts";

// 申请精选站点
export function applyLink(data: any) {
    return http({
        url: '/cloud-web/link/auth/apply',
        method: 'post',
        data: data
    })
}

// 查询精选站点列表
export function linkList() {
    return http({
        url: '/cloud-web/link/list',
        method: 'get'
    })
}
