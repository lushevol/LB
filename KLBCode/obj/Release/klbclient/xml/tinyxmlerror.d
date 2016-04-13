obj/Release/klbclient/xml/tinyxmlerror.o obj/Release/klbclient/xml/tinyxmlerror.d: src/xml/tinyxmlerror.cpp src/xml/tinyxml.h
	rm -f obj/Release/klbclient/xml/tinyxmlerror.d
	make -f make.do obj/Release/klbclient/xml/tinyxmlerror.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbclient/xml/tinyxmlerror.o src/xml/tinyxmlerror.cpp
%.h: fail
	
.PHONY:fail
fail:
	
