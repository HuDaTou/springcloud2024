import http from "@/utils/http.ts";

// 所有标签
export function tagList() {
    return http.get("/cloud-web/tag/list", {
        method: "get"
    });
}