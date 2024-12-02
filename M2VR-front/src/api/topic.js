import http from '@/utils/request'


export async function findAllTopics() {
  return http.get("/topic/findAllTopics")
}
