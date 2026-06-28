import http from "@/utils/http.ts";

// 获取文章详细
export const getArticleDetail = (id: string | string[]) => {
    return http.request({
        url: `/cloud-web/article/detail/${id}`,
        method: "get"
    });
}

// 获取评论
export const getComment = (type: number, typeId: number, pageNum: string, pageSize: string) => {
    return http.request({
        url: '/cloud-web/comment/getComment',
        method: "get",
        params: {
            type,
            typeId,
            pageNum,
            pageSize
        }
    });
}

// 用户添加评论
export const addComment = (data: object) => {
    return http.request({
        url: '/cloud-web/comment/auth/add/comment',
        method: "post",
        data
    });
}

// 时间轴
export const getTimeLine = () => {
    return http.request({
        url: '/cloud-web/article/timeLine',
        method: "get"
    });
}

// 查询不同类型下的文章列表
export function whereArticleList(type: Number, typeId: String) {
    return http.get(`/cloud-web/article/where/list/${typeId}`, {
        method: "get",
        params: {
            type
        }
    });
}

// 文章访问量+1
export function addArticleVisit(id: String) {
    return http.get(`/cloud-web/article/visit/${id}`, {
        method: "get"
    });
}

// 获取初始化时标题搜索数据
export function getSearchTitleList() {
    return http.get(`/cloud-web/article/search/init/title`, {
        method: "get"
    });
}

// 对内容进行文章搜索
export function searchArticleContent(content: String) {
    return http.get('/cloud-web/article/search/by/content', {
        params: {
            content
        },
        method: "get"
    });
}

// 搜索热门推荐
export function getHotRecommend() {
    return http.get(`/cloud-web/article/hot`, {
        method: "get"
    });
}
