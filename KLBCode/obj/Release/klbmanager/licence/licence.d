obj/Release/klbmanager/licence/licence.o obj/Release/klbmanager/licence/licence.d: src/licence/licence.cpp src/licence/cipher/cipher.h \
 src/licence/licence.h src/licence/share/include.h
	rm -f obj/Release/klbmanager/licence/licence.d
	make -f make.do obj/Release/klbmanager/licence/licence.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbmanager/licence/licence.o src/licence/licence.cpp
%.h: fail
	
.PHONY:fail
fail:
	
