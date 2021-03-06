PROBLEME := bin/Rtd.class bin/Minlexbfs.class bin/Disjcnt.class  bin/Rtd.class bin/Revedges.class

build: $(PROBLEME)

bin/%.class: src/%.java
	mkdir -p bin
	javac $^ -d bin

run-p1:      # nume necesar
	java -Xss128M -cp bin Minlexbfs

run-p2:      # nume necesar
	java -Xss128M -cp bin Disjcnt

run-p3:      # nume necesar
	java -Xss128M -cp bin Rtd

run-p4:      # nume necesar
	java -Xss128M -cp bin Revedges

clean:		 # nume necesar
	rm -rf bin
