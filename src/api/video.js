import http from '@/utils/request'


export async function findVideosByPages(pageNum) {
  return http.getRestApi("/video/findVideosByPages", pageNum)
}

export async function findAllVideos() {
  return http.get("/video/findAllVideos")
}
