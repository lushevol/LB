#ifndef __COMMANDPARSER_H__
#define __COMMANDPARSER_H__

#include <map>
#include <deque>

#include "model/model.h"

typedef void (*CompletionFunc)(StringCollection& result);
typedef std::deque<std::pair<String, bool> > OpList;

class Parser
{
    private:
        typedef std::map<String, int> OpMap;
        class Node
        {
            public:
                CompletionFunc Func;
                int AnyNext;
                IntCollection Empty;
                OpMap OP;
                bool End;
                Node();
        };
        typedef std::map<int, Node> NodeMap;
        NodeMap FMap;
        typedef std::set<const Node*> NodeSet;
        void GetMatchList(Node& node, OpMap& result, NodeSet& set, bool all);
        void GetMatchList(Node& node, OpMap& result, bool all);
    private:
        void BindEmpty(int from, int to);
        void BindOp(int from, int to, const char* op);
        void BindAny(int from, int to, CompletionFunc func = NULL);
    private:
        class Rule
        {
            public:
                Rule();
                enum
                {
                    Unknown,
                    And,//&
                    Or,//|
                    Any,//*
                    Little,//?
                    Op,//~
                    Value//%
                } Type;
                StringList Data;
                CompletionFunc Func;
        };
        typedef std::map<String, Rule> RuleMap;
        typedef std::vector<String> OpList;
        typedef std::map<String, bool> OrderRule;
        RuleMap FRules;
        int RandomNode();
        String RandomRule();
        void ExecuteRule(int left, int right, const Rule& rule);
        static int FindOp(const OpList& list, int left, int right, const char* op);
        void SetRule(const char* name, const OpList& op, int left, int right);
        void SetOrderRule(OStream& stream, OrderRule& order, int min, StringCollection& must, int count);
    protected:
        static const char* Root;
        void SetOpRule(const char* name, const char* op);
        void SetAndRule(const char* name, const char* andname);
        void SetOrRule(const char* name, const char* orname);
        void SetAnyRule(const char* name, const char* anyname);
        void SetLittleRule(const char* name, const char* littlename);
        void SetValueRule(const char* name, CompletionFunc func = NULL);
        void SetRule(const char* name, const char* rule);
        void SetOrderRule(const char* name, const char* ordername, int mincount, const char* mustname);
        void Init();
    public:
        Parser();
        void GetMatchCollection(const StringList& cmd, StringCollection& match);
        void ParseCmd(const StringList& cmd, ::OpList& list);
        static Parser Instance;
};

#endif // __COMPLETION_H__
