obj/Release/klbpackage/licence/licence.o obj/Release/klbpackage/licence/licence.d: src/licence/licence.cpp src/licence/cipher/cipher.h \
 src/licence/licence.h src/licence/share/include.h
	rm -f obj/Release/klbpackage/licence/licence.d
	make -f make.do obj/Release/klbpackage/licence/licence.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbpackage/licence/licence.o src/licence/licence.cpp
%.h: fail
	
.PHONY:fail
fail:
	
