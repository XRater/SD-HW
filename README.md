# Command Line Interface

This is app is an implementation of Command Line Interface. App consists of Parser and Interpreter.

  ## Parser
  
  This module designed to parse user input to instructions, which can e executed by CLI interpreter.
  Parser supports assignment instructions (such as `a=14`) and command instructions.
  
  Supported features:
  * multiple arguments for each command
  * pipeline with `|` symbol
  * emplacing variables with `$a` syntax
  * quoted and double-quoted tokens
  
  ## Interpreter
  
  This module works with instructions, which were provided by Parser. Instructions are executed consequently, therefore all
  changes are visible in interpreter scope after instruction execution.
  
  When dealing with pipeline interpreter consequently executes every unit command on output of previous one
  
  Currently supported commands:
  * `cat`
  * `pwd`
  * `echo`
  * `wc`
  * `exit`
  
  For every unknown command system call will be made
