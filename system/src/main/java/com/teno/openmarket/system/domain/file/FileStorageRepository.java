package com.teno.openmarket.system.domain.file;

import java.io.InputStream;

/**
 * [파일 저장소 도메인 리포지토리 인터페이스]
 * <p>
 * 구체적인 저장 기술(S3, 로컬 등)에 의존하지 않고 파일을 저장하는 기능을 정의합니다.
 */
public interface FileStorageRepository {

    /**
     * 파일 업로드
     * <p>
     * 파일을 저장소에 업로드합니다.
     *
     * @param inputStream 파일 데이터 스트림
     * @param fileName 저장될 파일명
     * @param contentType MIME 타입
     * @param size 파일 크기
     * @return 저장된 파일의 접근 가능한 Public URL
     */
    String upload(InputStream inputStream, String fileName, String contentType, long size);
}
