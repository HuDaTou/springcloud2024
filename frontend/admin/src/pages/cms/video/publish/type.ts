export interface CategoryType {
  id: number
  categoryName: string
}

export interface videoInfo {
  tagId: number[]
  categoryId: number | undefined
  videoCover: string | undefined
  video: string | undefined
  videoTitle: string | undefined
  description: string | undefined
  videoType: string | undefined
  permission: boolean | undefined
  videoSize: string | undefined

}

// 文章标签
export interface TagType {
  id: number
  tagName: string
}
