# Automata Conversion
Automata Conversions algorithms in Java

- epsilon-NFA to NFA
- epsilon-NFA to DFA
- NFA to DFA 

Conversions also can obtain convert to DFA Minimalistic form.

## Introduction
Non-deterministic finite automata(NFA) is a finite automata where 
for some cases when a specific input is given to the current state, 
the machine goes to multiple states or more than 1 states. It can contain ε move(epsilon-NFA).

On the other hand, in DFA, when a 
specific input is given to the current state, the machine 
goes to only one state. DFA has only one move on a given input symbol.

## Requirements
- Java SE Development Kit 8

## Usage
```
git clone https://github.com/islajd/Automata-Conversion.git
```
In our case git has cloned the project in home directory.

Create directory 'out' inside Automata-Conversion.
```
mkdir out
```
To compile project run the following inside the directory 'Automata-Conversion':
```
javac -sourcepath src -d out src/AutomataConversionApplication.java
```
To run the project use the commands below:
```
cd out/
java AutomataConversionApplication
```
## Inputs
Inputs are given in a simple UI.

Input templates are included in project path. (i.e. [Input](input))
## License
[MIT](LICENSE)
