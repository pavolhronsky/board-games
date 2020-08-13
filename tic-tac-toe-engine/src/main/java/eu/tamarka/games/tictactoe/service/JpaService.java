package eu.tamarka.games.tictactoe.service;

import java.io.Serializable;
import java.util.List;
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
    System.out.println("Searching for user: " + id);
    return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with ID=" + id + " does not exist."));
  }

  public void delete(E entity) {
    repository.delete(entity);
  }

  public List<E> findAll() {
    return repository.findAll();
  }
}
