artifacts:
	mkdir artifacts

asm: artifacts
	mkdir artifacts/asm

runtime: artifacts asm
	cd artifacts && gcc -c ../runtime/*.c 

program: clean runtime
	sbt "run $(src) ./artifacts/asm/program.nasm"
	nasm -f macho64 -o ./artifacts/program.o ./artifacts/asm/program.nasm
	gcc artifacts/*.o -o ./program

run: program
	./program

clean:
	rm -r artifacts/* || true
	rm ./program || true