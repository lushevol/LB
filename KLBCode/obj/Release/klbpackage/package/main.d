obj/Release/klbpackage/package/main.o obj/Release/klbpackage/package/main.d: src/package/main.cpp src/package/share/include.h \
 src/package/share/utility.h src/package/cipher/cipher.h \
 src/package/licence/licence.h src/package/licence/share/include.h
	rm -f obj/Release/klbpackage/package/main.d
	make -f make.do obj/Release/klbpackage/package/main.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbpackage/package/main.o src/package/main.cpp
%.h: fail
	
.PHONY:fail
fail:
	
