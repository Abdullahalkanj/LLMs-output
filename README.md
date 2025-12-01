#  Evaluating Prompt Design Strategies for Large Language Models in Bug Detection and Code Explanation  
*A Bachelor Thesis Project (2025)*

##  Overview  
This project investigates how different **prompt design strategies** influence the performance of **Large Language Models (LLMs)**—specifically *ChatGPT o4-mini*—in two software maintenance tasks:

- **Bug Detection & Correction**  
- **Code Explanation**

The experiment evaluates three prompting strategies:

1. **Basic Prompt**  
2. **Structured Prompt**  
3. **Chain-of-Thought (CoT) Prompt**

Each strategy includes three variations and is tested against **18 curated code examples**, yielding **162 total experimental runs**.

---

##  Research Questions  
1. **RQ1:** How do Basic, Structured, and Chain-of-Thought prompts affect the *success rate* of bug detection?  
2. **RQ2:** How do these prompt strategies affect the *accuracy* of code explanation?

---

##  Methodology  

###  Controlled Experiment  
A controlled experiment ensured that the **only changing variable** was the prompt strategy. Everything else remained constant:

- Same LLM (ChatGPT o4-mini)  
- Same temperature & parameters  
- Same dataset  
- Same evaluation method  

###  Dataset  
A total of **18 code examples** were used:

- **10 examples for bug detection** (with known bugs)  
- **8 examples for explanation** (clean code)

Sources included:

- QuixBugs dataset  
- FixEval dataset  
- University exercises  
- Textbooks  
- Self-created examples  

###  Evaluation  
**Bug Detection Metrics:**  
- **Success Rate** – whether all bugs were fixed  
- **Test Fix Accuracy** – % of bugs fixed  

**Code Explanation Metrics:**  
- **Explanation Accuracy** – checklist coverage  

---

##  Results Summary  

###  Bug Detection
| Prompt Strategy | Test Fix Accuracy | Success Rate |
|-----------------|-------------------|--------------|
| **Basic**       | 70.43%            | 43.33%       |
| **Structured**  | 82.16%            | 46.66%       |
| **Chain-of-Thought** | **91.11%** | **66.66%** |

➡ **Chain-of-Thought prompts show the best performance**, improving success rate by over **23%** compared to Basic prompts.

###  Code Explanation
| Prompt Strategy | Explanation Accuracy |
|-----------------|----------------------|
| **Basic**       | 92.79%               |
| **Structured**  | 97.42%               |
| **Chain-of-Thought** | **98.26%** |

➡ CoT prompts generate the **most complete and accurate explanations**.

---

##  Conclusions  
- Prompt design has a **significant impact** on LLM performance in software-related tasks.  
- **Chain-of-Thought** prompts outperform both Basic and Structured prompts across both evaluation tasks.  
- Structured prompts provide improvements but lack the deep reasoning ability of CoT.  
- Developers can achieve more reliable LLM outputs by using **guided and reasoning-based prompts**.

---

##  Future Work  
Potential improvements and extensions include:

- Evaluating multiple LLMs (e.g., GPT-4o, Claude, DeepSeek)  
- Testing prompts across different programming languages  
- Expanding tasks (refactoring, test generation, security auditing)  
- Automating the prompt evaluation pipeline  

---

##  Authors  
- **Abdullah Alkanj**  
- **Zaid Alajlani**  

Supervisor: *Arslan Musaddiq*  
Linnéuniversitetet – Växjö, Sweden  
Spring 2025  

---

## 📂 Suggested Repository Structure

