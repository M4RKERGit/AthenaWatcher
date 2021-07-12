package com.athena;

import org.springframework.data.repository.CrudRepository;

interface VisitsRepository extends CrudRepository<Visitor, Long> {}