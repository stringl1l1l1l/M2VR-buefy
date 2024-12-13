import http from '@/utils/request'

export async function findAllMarks() {
    return http.get("/mark/findAllMarks")
}

export async function findMarksByTopicId(topicId) {
    return http.getRestApi("/mark/findMarksByTopicId", topicId)
}

export async function findMarksByBegin(begin) {
    return http.getRestApi("/mark/findMarksByBegin", begin)
}

export async function insertMark(mark) {
    return http.post("/mark/insertMark", mark)
}

export async function findTodoBvidList(begin, topicId) {
    return http.getRestApi("/mark/findTodoBvidList", begin, topicId)
}

export async function isMarked(begin, end) {
    return http.getRestApi("/mark/isMarked", begin, end)
}
 
