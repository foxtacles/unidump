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

template<typename T> // constraint: comparable
class Sorter {

private:
	unsigned int sorts;
	typedef typename std::vector<T>::iterator it;
	typedef std::function<it(it, it)> piv_func;

	void quicksort_impl(it, it, const piv_func&);
	void reheap(std::vector<T>&, unsigned int, unsigned int);
	void buildheap(std::vector<T>&);

public:
	Sorter() = default;
	~Sorter() = default;

	unsigned int quicksort(std::vector<T>&);
	unsigned int insertsort(std::vector<T>&);
	unsigned int heapsort(std::vector<T>&);
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

template<typename T>
void Sorter<T>::reheap(std::vector<T>& data, unsigned int from, unsigned int to) {
	unsigned int u = (2 * from) + 1;

	if (u < to)
	{
		unsigned int maxson = u;

		if (!(u == to - 1 || (++sorts && data[u] > data[u + 1])))
			++maxson;

		if (data[from] < data[maxson])
		{
			std::swap(data[from], data[maxson]);
			reheap(data, maxson, to);
		}

		++sorts;
	}
}

template<typename T>
void Sorter<T>::buildheap(std::vector<T>& data) {
	unsigned int size = data.size();

	for (signed int i = (size / 2) - 1; i >= 0; --i)
		reheap(data, i, size);
}

template<typename T>
unsigned int Sorter<T>::heapsort(std::vector<T>& data) {
	if (data.size() < 2)
		return 0;

	sorts = 0;

	buildheap(data);

	for (unsigned int heapsize = data.size(); heapsize > 1; reheap(data, 0, heapsize))
		std::swap(data[0], data[--heapsize]);

	return sorts;
}

class SimpleKey : private Comparable<SimpleKey> {
private:
	unsigned int key, pos;

public:
	SimpleKey(unsigned int key, unsigned int pos) : key(key), pos(pos) {};
	SimpleKey() {}
	virtual ~SimpleKey() {};

	virtual bool operator==(const SimpleKey& u) { return this->key == u.key; }
	virtual bool operator<(const SimpleKey& u) { return this->key < u.key; }
	virtual bool operator>(const SimpleKey& u) { return this->key > u.key; }
};

class ExtendedKey : private Comparable<ExtendedKey> {
private:
	unsigned int key, pos;

public:
	ExtendedKey(unsigned int key, unsigned int pos) : key(key), pos(pos) {};
	ExtendedKey() {}
	virtual ~ExtendedKey() {};

	virtual bool operator==(const ExtendedKey& u) { return this->key == u.key && this->pos == u.pos; }
	virtual bool operator<(const ExtendedKey& u) { return this->key < u.key || (this->key == u.key && this->pos < u.pos); }
	virtual bool operator>(const ExtendedKey& u) { return this->key > u.key || (this->key == u.key && this->pos > u.pos); }
};

int main(int argc, char** argv) {
	std::vector<SimpleKey> v1, v1s;
	std::vector<ExtendedKey> v2, v2s;

	for (unsigned int i = 0; i < 10; ++i) {
		v1.emplace_back(i % 2, i);
		v2.emplace_back(i % 2, i);
	}

	v1s = v1;
	v2s = v2;

	Sorter<SimpleKey> s1;
	Sorter<ExtendedKey> s2;

	unsigned int s1_insert = s1.insertsort(v1);
	v1 = v1s;
	unsigned int s1_qsort = s1.quicksort(v1);
	v1 = v1s;
	unsigned int s1_hsort = s1.heapsort(v1);

	unsigned int s2_insert = s2.insertsort(v2);
	v2 = v2s;
	unsigned int s2_qsort = s2.quicksort(v2);
	v2 = v2s;
	unsigned int s2_hsort = s2.heapsort(v2);

	printf("SimpleKeys insertsort: %d, quicksort: %d, heapsort: %d\nExtendedKeys insertsort: %d, quicksort %d, heapsort: %d", s1_insert, s1_qsort, s1_hsort, s2_insert, s2_qsort, s2_hsort);

	return 0;
}
