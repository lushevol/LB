obj/Release/klbmanager/cipher/key.o obj/Release/klbmanager/cipher/key.d: src/cipher/key.cpp src/cipher/share/include.h src/cipher/cipher.h
	rm -f obj/Release/klbmanager/cipher/key.d
	make -f make.do obj/Release/klbmanager/cipher/key.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbmanager/cipher/key.o src/cipher/key.cpp
%.h: fail
	
.PHONY:fail
fail:
	
