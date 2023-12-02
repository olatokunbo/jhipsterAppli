package fr.omnilog.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.omnilog.domain.Valeur;
import fr.omnilog.repository.ValeurRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Valeur} entity.
 */
public interface ValeurSearchRepository extends ElasticsearchRepository<Valeur, Long>, ValeurSearchRepositoryInternal {}

interface ValeurSearchRepositoryInternal {
    Stream<Valeur> search(String query);

    Stream<Valeur> search(Query query);

    @Async
    void index(Valeur entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ValeurSearchRepositoryInternalImpl implements ValeurSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ValeurRepository repository;

    ValeurSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ValeurRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Valeur> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Valeur> search(Query query) {
        return elasticsearchTemplate.search(query, Valeur.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Valeur entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Valeur.class);
    }
}
