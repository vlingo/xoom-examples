package io.examples.order.data;

import javax.persistence.*;

@MappedSuperclass
public class Identity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }
    
}