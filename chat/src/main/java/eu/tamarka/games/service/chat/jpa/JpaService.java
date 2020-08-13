package eu.tamarka.games.service.chat.jpa;

import java.io.Serializable;
import javax.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaService<E, ID extends Serializable, R extends JpaRepository<E, ID>> {

  private R repository;

  public JpaService(R repository) {
    this.repository = repository;
  }

  public R getRepository() {
    return repository;
  }

  public E save(E entity) {
    return repository.save(entity);
  }

  public E findById(ID id) {
    return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with ID=" + id + " does not exist."));
  }

  protected void delete(E entity) {
    repository.delete(entity);
  }
}