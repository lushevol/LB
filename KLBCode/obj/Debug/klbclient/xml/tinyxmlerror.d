obj/Debug/klbclient/xml/tinyxmlerror.o obj/Debug/klbclient/xml/tinyxmlerror.d: src/xml/tinyxmlerror.cpp src/xml/tinyxml.h
	rm -f obj/Debug/klbclient/xml/tinyxmlerror.d
	make -f make.do obj/Debug/klbclient/xml/tinyxmlerror.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__DEBUG__ -c -o obj/Debug/klbclient/xml/tinyxmlerror.o src/xml/tinyxmlerror.cpp
%.h: fail
	
.PHONY:fail
fail:
	
