#include <iostream>
#include <sstream>
#include <cstring>
#include <cmath>
#include <cfloat>
#include <stdexcept>

const std::string newton = "x _ ((f) / (d))";
const std::string secant = "y _ ((f_y) * ((y _ x) / ((f_y) _ (f_x))))";
const unsigned int EVAL_PRECISION = 10;

std::string str_replace(const std::string& source, const char* find, const char* replace)
{
	unsigned int find_len = strlen(find);
	unsigned int replace_len = strlen(replace);
	unsigned int pos = 0;

	std::string dest = source;

	while ((pos = dest.find(find, pos)) != std::string::npos)
	{
		dest.replace(pos, find_len, replace);
		pos += replace_len;
	}

	return dest;
}

long double atomic_eval(char op, long double x, long double y)
{
	switch (op)
	{
		case '+': return x + y;
		case '_': return x - y;
		case '/': return x / y;
		case '*': return x * y;
		default:
			throw std::runtime_error("Unsupported operator");
	}
}

inline
bool DoubleCompare(long double a, long double b, long double epsilon)
{
	return fabs(a - b) < epsilon;
}

long double eval(const std::string& expr)
{
	std::stringstream x_str;
	x_str.precision(EVAL_PRECISION);
	std::string result = expr;
	size_t open, close;

	while ((open = result.find_last_of("h")) != std::string::npos)
	{
		close = result.find_first_of("+_*/() ", open + 1);

		x_str.seekg(0, std::ios::beg);
		x_str.str(result.substr(open + 1, close == std::string::npos ? close : close - open - 1));

		double e;
		x_str >> e;

		e = exp(e);

		x_str.seekg(0, std::ios::beg);
		x_str.str("");
		x_str << e;

		result.replace(open, close == std::string::npos ? close : close - open - 1, x_str.str());
	}

	while ((open = result.find_last_of("(")) != std::string::npos)
	{
		close = result.find_first_of(")", open + 1);

		if (close == std::string::npos)
			throw std::runtime_error("Malformed input, missing bracket");

		x_str.seekg(0, std::ios::beg);
		x_str.str("");

		if (close - open != 1)
			x_str << eval(result.substr(open + 1, close - open - 1));

		result.replace(open, close - open + 1, x_str.str());
	}

	while (!result.empty() && (open = result.find_first_of("+_*/")) != std::string::npos)
	{
		if (open + 1 == result.length())
			throw std::runtime_error("Malformed input, missing operand");

		x_str.seekg(0, std::ios::beg);
		x_str.str(str_replace(result.substr(0, open), " ", ""));

		if (x_str.str().empty())
			throw std::runtime_error("Malformed input, missing operand");

		long double x;
		x_str >> x;

		close = result.find_first_of("+_*/", open + 1);

		x_str.seekg(0, std::ios::beg);
		x_str.str(str_replace(result.substr(open + 1, close == std::string::npos ? close : close - open - 1), " ", ""));

		if (x_str.str().empty())
			throw std::runtime_error("Malformed input, missing operand");

		long double y;
		x_str >> y;

		long double R = atomic_eval(result[open], x, y);

		x_str.seekg(0, std::ios::beg);
		x_str.str("");
		x_str << R;

		result.replace(0, close == std::string::npos ? result.length() : close, x_str.str());
	}

	long double x_n;
	x_str.seekg(0, std::ios::beg);
	x_str.str(result);
	x_str >> x_n;

	return x_n;
}

long double evalGeneric(const std::string& f, long double f_x, long double x, unsigned long long n, long double e, std::string iteration = std::string())
{
	std::stringstream x_str;
	x_str.precision(EVAL_PRECISION);
	std::string base = (iteration.empty() ? f : iteration);

	x_str << x;
	long double x_n = x;
	unsigned long long i = 0;

	while (i < n && !DoubleCompare(eval(str_replace(f, "x", x_str.str().c_str())), f_x == DBL_MAX ? x_n : f_x, e))
	{
		std::string result = base;
		result = str_replace(result, "x", x_str.str().c_str());

		x_n = eval(result);

		x_str.seekg(0, std::ios::beg);
		x_str.str("");
		x_str << x_n;

		std::cout << "x_" << (i + 1) << ": " << x_n << "\n";

		++i;
	}

	std::cout << "stopped after " << i << " iterations.\n\n";

	return x_n;
}

long double evalNewton(const std::string& f, const std::string& d, long double f_x, long double x, unsigned long long n, long double e)
{
	std::string base = newton;

	base = str_replace(base, "f", f.c_str());
	base = str_replace(base, "d", d.c_str());

	return evalGeneric(f, f_x, x, n, e, base);
}

long double evalSecant(const std::string& f, long double f_x, long double x, long double y, unsigned long long n, long double e)
{
	std::string base = secant;
	std::string f_y = str_replace(f, "x", "y");

	base = str_replace(base, "f_x", f.c_str());
	base = str_replace(base, "f_y", f_y.c_str());

	std::stringstream x_str, y_str;
	x_str.precision(EVAL_PRECISION);
	y_str.precision(EVAL_PRECISION);

	x_str << x;
	y_str << y;
	long double x_n = x;
	long double y_n = y;
	unsigned long long i = 0;

	while (i < n && !DoubleCompare(eval(str_replace(f, "x", y_str.str().c_str())), f_x, e))
	{
		std::string result = base;
		result = str_replace(result, "x", x_str.str().c_str());
		result = str_replace(result, "y", y_str.str().c_str());

		x_n = y_n;
		y_n = eval(result);

		x_str.seekg(0, std::ios::beg);
		x_str.str("");
		x_str << x_n;

		y_str.seekg(0, std::ios::beg);
		y_str.str("");
		y_str << y_n;

		std::cout << "x_" << (i + 1) << ": " << y_n << "\n";

		++i;
	}

	std::cout << "stopped after " << i << " iterations.\n\n";

	return y_n;
}

inline
bool isValidDerivative(const std::string& input)
{
	return (input.find_first_not_of("+_*/.0123456789xh() ") == std::string::npos);
}

inline
bool isValidFunction(const std::string& input)
{
	return (isValidDerivative(input) && input.find_first_of("x") != std::string::npos);
}

int main(int argc, char* argv[])
{
	std::cout << "Some quick and naive implementation of Newton's method\n----------------------------------------------------------\n";
	std::cout << "allowed symbols are +, _, *, /, integers, floating point values, exponential function (examples: h4, hx), spaces, brackets, and one variable x\n";
	std::cout << "please note that this tool doesn't care for precedence of multiplcation/division so PUT BRACKETS\n\n";
	std::cout << "input your function: ";

	std::cout.precision(EVAL_PRECISION);
	std::cin.precision(EVAL_PRECISION);

	std::string input;
	std::getline(std::cin, input, '\n');

	if (!isValidFunction(input))
	{
		std::cout << "Error: No valid input\n";
		return 0;
	}

	std::cout << "input your function's first derivative (only required for newton): ";
	std::string dinput;
	std::getline(std::cin, dinput, '\n');

	if (!isValidDerivative(dinput))
	{
		std::cout << "Error: No valid input\n";
		return 0;
	}

	std::cout << "input the value of f(x) you seek (leave blank for f(x) = x): ";

	long double r;
	std::string _r;
	std::getline(std::cin, _r, '\n');

	if (_r.empty())
		r = DBL_MAX;
	else
	{
		std::stringstream x_str;
		x_str.precision(EVAL_PRECISION);
		x_str.str(_r);
		x_str >> r;
	}

	std::cout << "input your initial value for x: ";

	long double x;
	std::string _x;
	std::getline(std::cin, _x, '\n');

	if (_x.empty())
	{
		std::cout << "Error: No valid input\n";
		return 0;
	}
	else
	{
		std::stringstream x_str;
		x_str.precision(EVAL_PRECISION);
		x_str.str(_x);
		x_str >> x;
	}

	std::cout << "input your initial value for second x (only required for secant method): ";

	long double y;
	std::string _y;
	std::getline(std::cin, _y, '\n');

	if (_y.empty())
		y = DBL_MAX;
	else
	{
		std::stringstream y_str;
		y_str.precision(EVAL_PRECISION);
		y_str.str(_y);
		y_str >> y;
	}

	std::cout << "input the number of iterations: ";

	unsigned long long n;
	std::cin >> n;

	std::cout << "input epsilon: ";

	long double e;
	std::cin >> e;

	long double x_n;

	try {
		if (!dinput.empty())
			x_n = evalNewton(input, dinput, r, x, n, e);
		else if (y != DBL_MAX)
			x_n = evalSecant(input, r, x, y, n, e);
		else
			x_n = evalGeneric(input, r, x, n, e);
		std::cout << "Result: " << x_n;
	}
	catch (std::runtime_error& e) {
		std::cout << "Error: " << e.what();
	}

	std::cout << "\n";

#ifdef __WIN32__
	system("PAUSE");
#endif

	return 0;
}
