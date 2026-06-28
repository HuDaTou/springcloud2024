import http from "@/utils/http.ts";

// 添加树洞
export const addTreeHole = (content: String) => {
    return http.post("/cloud-web/treeHole/auth/addTreeHole", JSON.stringify({content}), {
        method: 'post'
    });
};

// 获取树洞列表
export const getTreeHoleList = () => {
    return http.get("/cloud-web/treeHole/getTreeHoleList", {
        method: 'get'
    });
};