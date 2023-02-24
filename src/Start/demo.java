package Start;

import Bean.Key;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class demo {
	public static void main(String[] args) {
		List<Key> keyList = new ArrayList<>();

		Key key = new Key();
		key.setKey("a");
		key.setPosition(1);
		keyList.add(key);

		key = new Key();
		key.setKey("b");
		key.setPosition(2);
		keyList.add(key);

		key = new Key();
		key.setKey("c");
		key.setPosition(3);
		keyList.add(key);

		List<String> keys = keyList.stream().map(key1 -> key1.getKey()).collect(Collectors.toList());
		for (String s : keys)
			System.out.println(s);
	}
}
