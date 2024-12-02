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

export async function findUnLabeledVediosByBvidList(bvidList) {
  return http.post("/video/findUnLabeledVediosByBvidList", bvidList)
}

export async function findNotMarkedBvidListWithTopicId(topicId) {
  return http.getRestApi("/video/findNotMarkedBvidListWithTopicId", topicId)
}

export async function setMarkByBvid(mark) {
  return http.put("/video/setMarkByBvid", mark)
}

export async function setMarkMaskByBvid(bvid, mask) {
  return http.getRestApi("/video/setMarkMaskByBvid", bvid, mask)
}

export async function setLabelByBvid(bvid, label) {
  return http.getRestApi("/video/setLabelByBvid", bvid, label)
}