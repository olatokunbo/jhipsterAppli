package fr.omnilog.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.omnilog.domain.UniteAdministrative;
import fr.omnilog.repository.UniteAdministrativeRepository;
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
 * Spring Data Elasticsearch repository for the {@link UniteAdministrative} entity.
 */
public interface UniteAdministrativeSearchRepository
    extends ElasticsearchRepository<UniteAdministrative, Long>, UniteAdministrativeSearchRepositoryInternal {}

interface UniteAdministrativeSearchRepositoryInternal {
    Page<UniteAdministrative> search(String query, Pageable pageable);

    Page<UniteAdministrative> search(Query query);

    @Async
    void index(UniteAdministrative entity);

    @Async
    void deleteFromIndexById(Long id);
}

class UniteAdministrativeSearchRepositoryInternalImpl implements UniteAdministrativeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final UniteAdministrativeRepository repository;

    UniteAdministrativeSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, UniteAdministrativeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<UniteAdministrative> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<UniteAdministrative> search(Query query) {
        SearchHits<UniteAdministrative> searchHits = elasticsearchTemplate.search(query, UniteAdministrative.class);
        List<UniteAdministrative> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(UniteAdministrative entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), UniteAdministrative.class);
    }
}
