package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    public void setUp(){
        vendorRepository= Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient=WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void getVendorList() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(
                        Vendor.builder().firstName("Cat1").build(),
                        Vendor.builder().firstName("Cat2").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void testGetVendorList() {
        BDDMockito.given(vendorRepository.findById("someID"))
                .willReturn(Mono.just(
                        Vendor.builder().firstName("Cat1").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/someID")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(1);
    }

    @Test
    public void testCreateVendor(){
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().firstName("Cat").build()));

        Mono<Vendor> vendorToSave =Mono.just(Vendor.builder().firstName("Some vend").build());

        webTestClient.post()
                .uri("/api/v1/vendors/")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdateVendor(){
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName("Update").build()));

        Mono<Vendor> vendorToUpdate =Mono.just(Vendor.builder().firstName("Some cat").build());

        webTestClient.put()
                .uri("/api/v1/vendors/dsada")
                .body(vendorToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}