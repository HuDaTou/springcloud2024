export interface CategoryType {
  id: number
  categoryName: string
}

export interface videoInfo {
  categoryId: number | undefined
  videoCover: string | undefined
  video: string | undefined
  videoTitle: string | undefined
  videoDescription: string | undefined
  videoType: string | undefined
  permission: string | undefined
  videoSize: string | undefined

}
