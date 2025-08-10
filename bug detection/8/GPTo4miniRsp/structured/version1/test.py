import unittest
import subprocess
import os
import glob

# === Absolute paths ===
BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..', '..'))
program_to_test = os.path.join(BASE_DIR, 'GPTo4miniRsp', 'structured', 'version1', 'excode.py')
in_folder  = os.path.join(BASE_DIR, 'E', 'in')
out_folder = os.path.join(BASE_DIR, 'E', 'out')
timeout_sec = 10

class TestBasicProgram(unittest.TestCase):
    passed_count = 0
    total_count = 0
    results = []  # list of dicts: name, passed

    def run_test_case(self, input_file, expected_output_file):
        # read input & expected (we still load expected, but won't display it)
        with open(input_file) as f_in, open(expected_output_file) as f_out:
            test_input      = f_in.read()
            expected_output = f_out.read().strip()

        proc = subprocess.run(
            ['python', program_to_test],
            input=test_input,
            text=True,
            capture_output=True,
            timeout=timeout_sec
        )
        actual_output = proc.stdout.strip()

        # normalize whitespace
        expected = "\n".join(line.strip() for line in expected_output.splitlines())
        actual   = "\n".join(line.strip() for line in actual_output.splitlines())

        # record totals
        TestBasicProgram.total_count += 1

        # use assertEqual but catch AssertionError
        try:
            self.assertEqual(actual, expected)
            passed = True
            TestBasicProgram.passed_count += 1
        except AssertionError:
            passed = False

        # store only name and pass/fail for the final table
        TestBasicProgram.results.append({
            'name':   os.path.basename(input_file),
            'passed': passed
        })

    def test_all_cases(self):
        """Run every .txt from in/ against out/"""
        inputs = sorted(glob.glob(os.path.join(in_folder, '*.txt')))
        for inp in inputs:
            outp = os.path.join(out_folder, os.path.basename(inp))
            self.run_test_case(inp, outp)

    @classmethod
    def tearDownClass(cls):
        # header
        print("\n## Test Summary")
        print(f"Program: `{program_to_test}`")
        if cls.total_count == 0:
            print("No tests found.\n")
            return

        rate = cls.passed_count / cls.total_count * 100
        print(f"Passed: {cls.passed_count}/{cls.total_count} ({rate:.2f}%)\n")

        # Markdown table with only name & result
        print("| Test Name       | Result |")
        print("|-----------------|--------|")
        for r in cls.results:
            status = "PASS" if r['passed'] else "FAIL"
            print(f"| {r['name']:<15} | {status} |")

if __name__ == '__main__':
    unittest.main(verbosity=0)
