import { Minio } from 'minio-js'

const minioClient = new Minio.Client({
    endPoint: import.meta.env.VITE_MINIO_BASE_HOST,
    port: parseInt(import.meta.env.VITE_MINIO_API_PORT),
    useSSL: false,
    accessKey: 'e9lQzgHpaPwzDIoTUGXd',
    secretKey: 'GPyjIi7YmEqiSrArTL9moV4E6p60dhKivLN678lj'
});


export async function getObjectsUrls(bucketName, dirPrefix) {
    const urls = [];
    const stream = minioClient.listObjects(bucketName, dirPrefix, true);
    return new Promise((resolve, reject) => {
        stream.on('data', (obj) => {
            // 拼接原始 URL
            const originalUrl = `http://${import.meta.env.VITE_MINIO_BASE_HOST}:${import.meta.env.VITE_MINIO_API_PORT}/${bucketName}/${obj.name}`;
            urls.push(originalUrl);
        });

        stream.on('end', () => {
            resolve(urls);  // 返回 URL 数组
        });

        stream.on('error', (err) => {
            reject(err);  // 发生错误时 reject
        });
    });
}

export async function minioIsBucketExist(bucketName) {
    try {
        const exists = await minioClient.bucketExists(bucketName)
        console.log('Success', exists)
    } catch (err) {
        console.log(err.message)
    }
}

export async function minioListBuckets() {
    try {
        const buckets = await minioClient.listBuckets()
        console.log('Success', buckets)
    } catch (err) {
        console.log(err.message)
    }
}