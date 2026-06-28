import http from "@/utils/http.ts";

// 查询留言板列表
export function getLeaveWordList(id?: any) {
    return http({
        url: '/cloud-web/leaveWord/list',
        method: 'get',
        params: {
            id
        }
    })
}

// 新增留言板
export function userLeaveWord(content: any) {
    return http.post('/cloud-web/leaveWord/auth/userLeaveWord',JSON.stringify(content))
}