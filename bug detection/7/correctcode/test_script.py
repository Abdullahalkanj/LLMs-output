import os
import difflib
import unittest
import subprocess
from termcolor import colored

class TestBasicProgram(unittest.TestCase):
    BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
    print(BASE_DIR)
    program_path = os.path.join(BASE_DIR, 'correctcode', 'corrected.py')
    input_dir = os.path.join(BASE_DIR, 'F', 'in')
    output_dir = os.path.join(BASE_DIR, 'F', 'out')
    timeout_sec = 5

    passed_tests = []
    failed_tests = {}
    timed_out_tests = []

    def run_test_case(self, input_path, expected_output_path):
        with open(input_path, "r", encoding="utf-8") as f:
            input_data = f.read()
        with open(expected_output_path, "r", encoding="utf-8") as f:
            expected_output = f.read().strip()

        basename = os.path.basename(input_path)
        try:
            result = subprocess.run(
                ["python", self.program_path],
                input=input_data,
                capture_output=True,
                text=True,
                timeout=self.timeout_sec
            )
            actual_output = result.stdout.strip()
            if actual_output != expected_output:
                diff = "\n".join(
                    difflib.unified_diff(
                        expected_output.splitlines(),
                        actual_output.splitlines(),
                        fromfile='Expected',
                        tofile='Actual',
                        lineterm=''
                    )
                )
                self.__class__.failed_tests[basename] = diff
            else:
                self.__class__.passed_tests.append(basename)
        except subprocess.TimeoutExpired:
            self.__class__.timed_out_tests.append(basename)

    def test_all_cases_individually(self):
        input_files = sorted(
            f for f in os.listdir(self.input_dir) if f.endswith(".txt")
        )
        for filename in input_files:
            input_path = os.path.join(self.input_dir, filename)
            output_path = os.path.join(self.output_dir, filename)
            self.run_test_case(input_path, output_path)

    @classmethod
    def tearDownClass(cls):
        total = len(cls.passed_tests) + len(cls.failed_tests) + len(cls.timed_out_tests)
        print("\n================== SUMMARY ==================")
        print(f"Program: {cls.program_path}")
        print(f"Tests passed: {len(cls.passed_tests)}/{total}")
        print(f"Pass rate: {100 * len(cls.passed_tests) / total:.2f}%\n")

        if cls.failed_tests:
            print(colored(f"❌ Failed Tests ({len(cls.failed_tests)}):", "red"))
            for name in cls.failed_tests:
                print(f"- {name}")
            print()

        if cls.timed_out_tests:
            print(colored(f"⏰ Timed Out Tests ({len(cls.timed_out_tests)}):", "yellow"))
            for i in range(0, len(cls.timed_out_tests), 5):
                print("- " + ", ".join(cls.timed_out_tests[i:i+5]))
            print()

        if cls.failed_tests:
            print(colored("🔍 Diffs (showing only differing lines):\n", "cyan"))
            for name, diff in cls.failed_tests.items():
                print(colored(f"❌ {name}", "red"))
                print(colored(diff, "white"))
                print()
        print("=============================================\n")

if __name__ == "__main__":
    unittest.main(verbosity=0)
