package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @BeforeEach
    public void setUp(){
        categoryRepository= Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient=WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void getAll(){
        given(categoryRepository.findAll())
                .willReturn(Flux.just(
                        Category.builder().description("Cat1").build(),
                        Category.builder().description("Cat2").build()));

        webTestClient.get()
                .uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);

    }

    @Test
    public void getById(){
        given(categoryRepository.findById("someID"))
                .willReturn(Mono.just(
                        Category.builder().description("Cat").build()));

        webTestClient.get()
                .uri("/api/v1/categories/someID")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(1);
    }

    @Test
    public void testCreateCategory(){
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().description("Cat").build()));

        Mono<Category> catToSave =Mono.just(Category.builder().description("Some cat").build());

        webTestClient.post()
                .uri("/api/v1/categories/")
                .body(catToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdateCategory(){
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().description("Update").build()));

        Mono<Category> catToUpdate =Mono.just(Category.builder().description("Some cat").build());

        webTestClient.put()
                .uri("/api/v1/categories/dsada")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void testPatchCategoryNoChanges(){
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate =Mono.just(Category.builder().build());

        webTestClient.patch()
                .uri("/api/v1/categories/dsadsada")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, Mockito.never()).save(any());
    }

    public void testPatchCategoryWithChanges(){
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate =Mono.just(Category.builder().description("updates").build());

        webTestClient.patch()
                .uri("/api/v1/categories/dsadsada")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any());
    }
}