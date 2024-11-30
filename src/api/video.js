import http from '@/utils/request'


export async function findVideosByPages(pageNum) {
  return http.getRestApi("/video/findVideosByPages", pageNum)
}

export async function findAllVideos() {
  return http.get("/video/findAllVideos")
}

export async function findVideosByTopicId(topicId) {
  return http.getRestApi("/video/findVideosByTopicId", topicId)
}

export async function findVideoByBvid(bvid) {
  return http.getRestApi("/video/findVideoByBvid", bvid)
}

export async function findNotMarkedBvidListWithTopicId(topicId) {
  return http.getRestApi("/video/findNotMarkedBvidListWithTopicId", topicId)
}

export async function setMarkedByBvid(bvid) {
  return http.put("/video/setMarkedByBvid", { bvid: bvid })
}
