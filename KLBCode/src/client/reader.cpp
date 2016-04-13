#include <iostream>

#include <readline/readline.h>
#include <readline/history.h>
#include <signal.h>
#include <termios.h>
#include <unistd.h>

#include "reader.h"
#include "control.h"
#include "parser.h"

using namespace std;

inline char* CopyString(const String& str)
{
    char* res = (char*)malloc((str.size() + 1) * sizeof(char));
    memcpy(res, str.c_str(), str.size() + 1);
    return res;
}

inline char** CopyStringList(const StringList& list)
{
    char** res = (char**)malloc((list.size() + 1) * sizeof(char*));
    char** ch = res;
    ENUM_STL_CONST(StringList, list, e)
    {
        *ch = CopyString(*e);
        ++ch;
    }
    *ch = NULL;
    return res;
}

inline void FreeStringList(char** list)
{
    char** ch = list;
    while(*ch != NULL)
    {
        free(*ch);
        ++ch;
    }
    free(list);
}

void Reader::Init()
{
    rl_completion_entry_function = CompletionPath;
    rl_completion_display_matches_hook = Display;
    BindKey();
    using_history();
}

void Reader::BindKey()
{
    rl_bind_key('\t', rl_complete);
    rl_bind_keyseq("^[[A", rl_get_next_history);
    rl_bind_keyseq("^[[B", rl_get_previous_history);
}

void Reader::UnbindKey()
{
    rl_unbind_key('\t');
    rl_bind_keyseq("^[[A", NULL);
    rl_bind_keyseq("^[[B", NULL);
}

void Reader::Display(char** matches, int len, int max)
{
    StringList list;
    if(*matches)
    {
        list.push_back(*matches);
        matches++;
    }
    while(*matches)
    {
        if(**matches)
            list.push_back(*matches);
        ++matches;
    }
    if(!list.empty())
    {
        matches = CopyStringList(list);
        rl_display_match_list(matches, list.size(), max);
        FreeStringList(matches);
    }
    rl_forced_update_display();
}

void Reader::SetDisplayMode(bool enable)
{
    struct termios term;
    tcgetattr(0, &term);
    static const int ECHOFLAGS = ECHO | ECHOE | ECHOK | ECHONL;
    if(enable)
        term.c_lflag |= ECHOFLAGS;
    else
        term.c_lflag &= ~ECHOFLAGS;
    tcsetattr(0, TCSAFLUSH, &term);
}

void Reader::ReadPassword(String& password)
{
    SetDisplayMode(false);
    UnbindKey();
    char* pass = readline(NULL);
    if(pass)
    {
        password = pass;
        PRINTF("Input password:" << password);
        free(pass);
    }
    BindKey();
    SetDisplayMode(true);
}

void Reader::Read(const char* prompt, String& result)
{
    char* t = readline(prompt);
    if(t)
    {
        result = t;
        free(t);
    }
}

void Reader::ReadCommand(String& result)
{
    Read("> ", result);
    if(!result.empty())
        add_history(result.c_str());
}

void Reader::ReadNoTab(const char* prompt, String& result)
{
    UnbindKey();
    Read(prompt, result);
    BindKey();
}

char* Reader::CompletionPath(const char* text, int state)
{
    static StringList list;
    if(state == 0)
    {
        StringList cmd;
        Command::GenerateOps(String(rl_line_buffer).substr(0, rl_point).c_str(), cmd);
        if(!cmd.empty() && cmd.back() == text)
            cmd.pop_back();
        StringCollection match;
        Parser::Instance.GetMatchCollection(cmd, match);
        ENUM_STL(StringCollection, match, e)
        {
            list.push_back(*e);
        }
    }
    char* res = NULL;
    while(!res && !list.empty())
    {
        String& word = list.front();
        unsigned int len = strlen(text);
        if(!word.empty() || *text)
        {
            if(len <= word.size() && strncmp(word.c_str(), text, len) == 0)
                res = CopyString(word);
        } else
            res = CopyString("");
        list.pop_front();
    }
    return res;
}

