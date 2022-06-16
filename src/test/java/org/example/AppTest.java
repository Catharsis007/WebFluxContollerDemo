package org.example;


import org.junit.jupiter.api.BeforeEach;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Flow;
import java.util.stream.Stream;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private List<String> dataValues = Arrays.asList("The quick brown fox jumps over the lazy dog".split(" "));
    private Flux dataSrc;
    @BeforeEach
    public void setupSource(){
        dataSrc = Flux.fromIterable(dataValues);
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        Flux<Integer> publisher = Flux.range(1,100);
        publisher.subscribe(System.out::println);
    }

    @Test
    public void publishedWithStream(){
        Flux<UUID> pub = Flux.fromStream(Stream.generate(UUID::randomUUID).limit(100));
        pub.subscribe(System.out::println);
    }

    @Test
    public void verifyPublishSequence(){
        StepVerifier.create(this.dataSrc).expectNextSequence(this.dataValues).verifyComplete();
        this.dataSrc.collectMap(s-> s, s -> s.toString()+ s.toString().length()).subscribe(v-> System.out.println(v));
    }
    @Test
    public void testSequence(){
        StepVerifier.create(this.dataSrc).expectNext("The").expectNext("quick").
                expectNext("brown").expectNext("fox").expectNext("jumps").expectNext("over").expectNext("the", "lazy","dog").expectComplete();
    }

    @Test
    public void TestSequence2(){

        StepVerifier.create(this.dataSrc).expectNext("The").thenConsumeWhile(s-> s!=null, System.out::println).verifyComplete();
    }

    @Test
    public void errorSequence(){
        var pub = Flux.range(1,10).map(s-> {
            if (s==8) {
                throw new IllegalArgumentException("my exception");
            }
            return s;}).onErrorReturn(ex-> {System.err.println("publisher: "+ex);return true;},0);
        pub.subscribe(s-> System.out.println(s),ex-> System.err.println(ex.getMessage()));

    }
}
