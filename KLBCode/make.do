#file list....
#/bin
#/obj
#/src
#Makefile

#CC:=

#DEFINE:=
#CPPFLAGS:=
#INCLUDES:=

#LINKFLAGS:=
#LIBS:=

#TARGET:=
#BIN:=

#SOURCES:=server/hello.cpp test/test.cpp

ifndef CC
CC:=g++
endif

FILES:=$(addprefix obj/$(TARGET)/$(BIN)/,$(basename $(SOURCES)))
OBJECTS:=$(addsuffix .o,$(FILES))
DEPS:=$(addsuffix .d,$(FILES))
PHONYS:=$(addsuffix .phony,$(FILES))

LINK:=bin/$(TARGET)/$(BIN)

.PHONY:domake
domake: $(DEPS) $(PHONYS) $(LINK)

$(LINK): $(OBJECTS)
	mkdir -p bin/$(TARGET)
	$(CC) -o $@ $(OBJECTS) $(LINKFLAGS) $(LIBS)

%.phony: %.d
	make -f $<

obj/$(TARGET)/$(BIN)/%.d: src/%.cpp
	mkdir -p $(dir $@); \
	$(CC) -MM $(DEFINE) $< > $@.$$$$; \
	sed 's,\($(notdir $*)\)\.o[ :]*,obj/$(TARGET)/$(BIN)/$*.o obj/$(TARGET)/$(BIN)/$*.d: ,g' < $@.$$$$ > $@; \
	rm -f $@.$$$$
	echo '\trm -f $@' >> $@
	echo '\tmake -f make.do $@ -e CC:=\0047$(CC)\0047 CPPFLAGS:=\0047$(CPPFLAGS)\0047 INCLUDES:=\0047$(INCLUDES)\0047 DEFINE:=\0047$(DEFINE)\0047' >> $@
	echo '\t$(CC) $(CPPFLAGS) $(INCLUDES) $(DEFINE) -c -o $(basename $@).o $<' >> $@
	echo '%.h: fail' >> $@
	echo '\t' >> $@
	echo '.PHONY:fail' >> $@
	echo 'fail:' >> $@
	echo '\t' >> $@

obj/$(TARGET)/$(BIN)/%.d: src/%.c
	mkdir -p $(dir $@); \
	$(CC) -MM $(DEFINE) $< > $@.$$$$; \
	sed 's,\($(notdir $*)\)\.o[ :]*,obj/$(TARGET)/$(BIN)/$*.o obj/$(TARGET)/$(BIN)/$*.d: ,g' < $@.$$$$ > $@; \
	rm -f $@.$$$$
	echo '\trm -f $@' >> $@
	echo '\tmake -f make.do $@ -e CC:=\0047$(CC)\0047 CPPFLAGS:=\0047$(CPPFLAGS)\0047 INCLUDES:=\0047$(INCLUDES)\0047 DEFINE:=\0047$(DEFINE)\0047' >> $@
	echo '\t$(CC) $(CPPFLAGS) $(INCLUDES) $(DEFINE) -c -o $(basename $@).o $<' >> $@
	echo '%.h: fail' >> $@
	echo '\t' >> $@
	echo '.PHONY:fail' >> $@
	echo 'fail:' >> $@
	echo '\t' >> $@

.PHONY:clean
clean:
	rm -rf bin/$(TARGET)/$(BIN)
	rm -rf obj/$(TARGET)/$(BIN)
