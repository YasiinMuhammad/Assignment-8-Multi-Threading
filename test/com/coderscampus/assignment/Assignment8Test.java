package com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class Assignment8Test {

	@Test
	void test() {
		Assignment8 assignment8 = new Assignment8();

		List<Integer> allNumbers = Collections.synchronizedList(new ArrayList<>());
// CachedThreadPool did not give me 1000000 numbers
//		ExecutorService service = Executors.newCachedThreadPool();
		ExecutorService service = Executors.newFixedThreadPool(1000000);

		List<CompletableFuture<Void>> task = new ArrayList<>(1000);

		for (int i = 0; i < 1000; i++) {

			task.add(CompletableFuture.supplyAsync(() -> assignment8.getNumbers(), service)
					.thenAccept(numbers -> allNumbers.addAll(numbers)));
		}
		while (task.stream().filter(CompletableFuture::isDone).count() < 1000) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Done fetching all numbers " + allNumbers.size());

			Map<Integer, Long> map = allNumbers.stream()
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			System.out.println(map);
		}
	}
}
