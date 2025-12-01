package kg.Isagulova.spring_eshop.dao;


import kg.Isagulova.spring_eshop.domain.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
}
