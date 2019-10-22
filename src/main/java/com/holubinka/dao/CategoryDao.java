package com.holubinka.dao;

import com.holubinka.model.TaskManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<TaskManager, Long> {

}
