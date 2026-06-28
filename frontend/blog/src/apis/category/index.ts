import http from "@/utils/http.ts";

// 查询分类列表
export function categoryList() {
    return http.get("/cloud-web/category/list", {
        method: "get"
    });
}