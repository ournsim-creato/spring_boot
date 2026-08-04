package com.spring_boot_api_p2.domain.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "menu")
@Data
public class Menu extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String path;

    private String redirect;

    @Column(name = "always_show")
    private Boolean alwaysShow = false;

    private Boolean hidden = false;

    private String title;

    private String icon;

    @Column(name = "no_cache")
    private Boolean noCache = false;

    @Column(name = "title_key")
    private String titleKey;

    private String link;

    private String component;

    @Column(name = "sort_order")
    private Integer sortOrder;

    // Self relationship: Menu -> Parent Menu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;
}
