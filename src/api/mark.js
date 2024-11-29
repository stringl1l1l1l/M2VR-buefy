import http from '@/utils/request'

export async function findAllMarks() {
    return http.get("/mark/findAllMarks")
}

export async function findMarksByTopicId(topicId) {
    return http.getRestApi("/mark/findMarksByTopicId", topicId)
}

export async function insertMark(mark) {
    return http.post("/mark/insertMark", mark)
}

export async function findTodoBvidList(bvid, topicId) {
    return http.getRestApi("/mark/findTodoBvidList", bvid, topicId)
}
