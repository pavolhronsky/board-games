package eu.tamarka.authorization.jpa;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaService<E, ID, R extends JpaRepository<E, ID>> {

  private R repository;

  public JpaService(R repository) {
    this.repository = repository;
  }

  protected E save(E entity) {
    return repository.save(entity);
  }

  protected E findById(ID id) {
    Optional<E> entity = repository.findById(id);
    return entity.orElseThrow(() -> new EntityNotFoundException("Entity " + this.getClass().getName() + " with ID=" + id + " does not exist."));
  }

  protected void delete(E entity) {
    repository.delete(entity);
  }

  protected Page<E> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }
}