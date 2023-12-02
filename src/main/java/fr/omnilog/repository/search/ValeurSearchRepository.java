package fr.omnilog.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.omnilog.domain.Valeur;
import fr.omnilog.repository.ValeurRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Valeur} entity.
 */
public interface ValeurSearchRepository extends ElasticsearchRepository<Valeur, Long>, ValeurSearchRepositoryInternal {}

interface ValeurSearchRepositoryInternal {
    Page<Valeur> search(String query, Pageable pageable);

    Page<Valeur> search(Query query);

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
    public Page<Valeur> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Valeur> search(Query query) {
        SearchHits<Valeur> searchHits = elasticsearchTemplate.search(query, Valeur.class);
        List<Valeur> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
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
