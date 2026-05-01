package io.github.siddharthbhansalidev.tickerquery.repository;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import io.github.siddharthbhansalidev.tickerquery.interfaces.PriceCache;
import io.github.siddharthbhansalidev.tickerquery.model.DateRange;
import io.github.siddharthbhansalidev.tickerquery.model.PriceData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(name = "price-cache", havingValue = "gcs")
public class GcsCache implements PriceCache {

    private final String bucketName;
    private final Storage storage;
    private final ObjectMapper mapper;

    public GcsCache(@Value("${gcs-bucket-name:ticker-query}") String bucketName, Storage storage, ObjectMapper mapper) {
        this.bucketName = bucketName;
        this.storage = storage;
        this.mapper = mapper;
    }

    @Override
    public Map<LocalDate, PriceData> get(String ticker, DateRange range) {
        try {
            Blob blob = storage.get(BlobId.of(bucketName, generateKey(ticker, range)));

            if (blob != null && blob.exists()) {
                return mapper.readValue(blob.getContent(), new TypeReference<>() {});
            }
        } catch (Exception e) {
            log.error("Failed to read from GCS cache.", e);
        }

        return null;
    }

    @Override
    public void put(String ticker, DateRange range, Map<LocalDate, PriceData> data) {
        try {
            byte[] bytes = mapper.writeValueAsBytes(data);
            BlobId blobId = BlobId.of(bucketName, generateKey(ticker, range));
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/json").build();
            storage.create(blobInfo, bytes);
        } catch (Exception e) {
            log.error("Failed to write to GCS cache.", e);
        }
    }

}
