#include <vector>
#include <functional>
#include <algorithm>
#include <ctime>
#include <cstdlib>
#include <cstdint>
#include <cstdio>

template<typename T>
class Comparable {

protected:
	Comparable() = default;

public:
	virtual ~Comparable() {};
	virtual bool operator==(const T&) = 0;
	virtual bool operator!=(const T& t) { return !operator==(t); };
	virtual bool operator<(const T&) = 0;
	virtual bool operator>(const T&) = 0;
	virtual bool operator<=(const T& t) { return operator<(t) || operator==(t); };
	virtual bool operator>=(const T& t) { return operator>(t) || operator==(t); };
};

class UInt32 : private Comparable<UInt32> {

public:
	uint32_t u;

public:
	UInt32() = default;
	UInt32(uint32_t u) : u(u) {}
	virtual ~UInt32() {};

	virtual bool operator==(const UInt32& u) { return this->u == u.u; }
	virtual bool operator<(const UInt32& u) { return this->u < u.u; }
	virtual bool operator>(const UInt32& u) { return this->u > u.u; }
};

template<typename T> // constraint: comparable
class Sorter {

private:
	unsigned int sorts;
	typedef typename std::vector<T>::iterator it;
	typedef std::function<it(it, it)> piv_func;
	void quicksort_impl(it, it, const piv_func&);

public:
	Sorter() = default;
	~Sorter() = default;

	unsigned int quicksort(std::vector<T>&);
	unsigned int randqsort(std::vector<T>&);
	unsigned int insertsort(std::vector<T>&);
};

template<typename T>
void Sorter<T>::quicksort_impl(it first, it last, const piv_func& func) {
	it i = first, j = last;
	T pivot = *func(i, j);

	while (i <= j) {
		while (*i < pivot)
		{
			++i;
			++sorts;
		}

		while (*j > pivot)
		{
			--j;
			++sorts;
		}

		if (i <= j) {
			std::swap(*i, *j);
			++i;
			--j;
		}
	}

	if (first < j)
		quicksort_impl(first, j, func);
	if (i < last)
		quicksort_impl(i, last, func);
}

template<typename T>
unsigned int Sorter<T>::quicksort(std::vector<T>& data) {
	if (data.size() < 2)
		return 0;

	sorts = 0;

	quicksort_impl(data.begin(), --data.end(), [](it first, it last)
	{
		first += ((std::distance(first, last) + 1) / 2);
		return first;
	});

	return sorts;
}

template<typename T>
unsigned int Sorter<T>::randqsort(std::vector<T>& data) {
	if (data.size() < 2)
		return 0;

	std::srand(time(nullptr));
	sorts = 0;

	quicksort_impl(data.begin(), --data.end(), [](it first, it last)
	{
		unsigned int max = std::distance(first, last) + 1;
		first += (std::rand() % max);
		return first;
	});

	return sorts;
}

template<typename T>
unsigned int Sorter<T>::insertsort(std::vector<T>& data) {
	if (data.size() < 2)
		return 0;

	sorts = 0;

	unsigned int i, j;
	T n;

	for (i = 1; i <= data.size() - 1; ++i) {
		n = data[i];

		for (j = i; j > 0 && ++sorts && data[j - 1] > n; --j)
			data[j] = data[j - 1];

		data[j] = n;
	}

	return sorts;
}

int main(int argc, char** argv) {

	if (argc < 2)
		return 0;

	std::vector<UInt32> v1, v2, v3, v1s, v2s, v3s;
	unsigned int n = atoi(argv[1]);

	for (unsigned int i = 1; i <= n; ++i)
	{
		v1.push_back(i);
		v3.push_back(i);

		v2.insert(v2.begin(), i);
	}

	std::srand(time(nullptr));
	std::random_shuffle(v3.begin(), v3.end(), [](ptrdiff_t i) { return std::rand() % i; });

	v1s = v1;
	v2s = v2;
	v3s = v3;

	Sorter<UInt32> s;

	unsigned int v1_quicksort = s.quicksort(v1);
	unsigned int v2_quicksort = s.quicksort(v2);
	unsigned int v3_quicksort = s.quicksort(v3);

	const unsigned int count_rand = 100;
	unsigned int v1_randqsort = 0, v2_randqsort = 0, v3_randqsort = 0;

	for (unsigned int i = 0; i < count_rand; ++i)
	{
		v1 = v1s;
		v2 = v2s;
		v3 = v3s;
		v1_randqsort += s.randqsort(v1);
		v2_randqsort += s.randqsort(v2);
		v3_randqsort += s.randqsort(v3);
	}

	v1_randqsort /= count_rand;
	v2_randqsort /= count_rand;
	v3_randqsort /= count_rand;

	v1 = v1s;
	v2 = v2s;
	v3 = v3s;

	unsigned int v1_insertsort = s.insertsort(v1);
	unsigned int v2_insertsort = s.insertsort(v2);
	unsigned int v3_insertsort = s.insertsort(v3);

	printf("Random permutation\n\tquicksort: %d\n\trandqsort: %d\n\tinsertsort: %d\n\n(1, ..., %d)\n\tquicksort: %d\n\trandqsort: %d\n\tinsertsort: %d\n\n(%d, ..., 1)\n\tquicksort: %d\n\trandqsort: %d\n\tinsertsort: %d\n", v3_quicksort, v3_randqsort, v3_insertsort, n, v1_quicksort, v1_randqsort, v1_insertsort, n, v2_quicksort, v2_randqsort, v2_insertsort);

	return 0;
}